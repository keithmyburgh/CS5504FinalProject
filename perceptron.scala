package boom.ifu

import chisel3._
import chisel3.util._

import freechips.rocketchip.config.{Field, Parameters}
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._

import boom.common._
import boom.ifu._
import boom.exu._
import boom.util._

class PerceptronBranchPredictorBank(n_percs: Int)(implicit p: Parameters) extends BranchPredictorBank()(p)
    with HasBoomFrontendParameters
{
    val mems            = Nil
    val percs           = RegInit(VecInit.fill(n_percs) ( VecInit.fill(globalHistoryLength)(0.S(8.W)) ))
    val hash_idx        = (io.update.bits.pc.hashCode % n_percs).U(8.W)
    val ghist           = io.update.bits.ghist.asBools.map { x => Mux(x.asSInt === 0.S, -1.S(8.W), 1.S(8.W))}
    val train_coef      = ghist(0)
    // The number we use to scale the perc's weights during training
    // if the last ghist bit was 0, branch was not taken and scale by -1 otherwise scale by 1
    val training_threshold  = RegInit((1.93 * globalHistoryLength + 14.0).floor.round.asSInt)
    val last_pred           = RegInit(1.S(8.W))
    val last_hash           = RegInit(hash_idx)
    for (w <- 0 until bankWidth) {
        val pred    = RegInit(percs(hash_idx).zip(ghist).map { case (w, g) => { w * g }}.reduce(_ + _))
        // if the dot + bias is negative, predict not taken (-1) otherwise predict taken (1)
        val w_pred  = Mux( pred < 0.S, -1.S(8.W), 1.S(8.W) )
        // Perform training if magnatude of prediction is less then threshold
        // OR if the sign of our prediction does not match the sign of some bit in the global history
        when (w_pred.abs <= training_threshold || last_pred =/= train_coef) {
            val updated_weights = percs(last_hash).zip(ghist).map { case (w, gb) => w * train_coef + gb}
            percs(last_hash) := updated_weights
        }
        last_pred           := RegNext(w_pred)
        last_hash           := RegNext(hash_idx)
        // Pass the prediction to the response
        io.resp.f3(w).taken := Mux(w_pred === 1.S(8.W), true.B, false.B)
    }
}