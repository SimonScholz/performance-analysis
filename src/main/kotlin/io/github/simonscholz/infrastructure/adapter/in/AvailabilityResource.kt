package io.github.simonscholz.infrastructure.adapter.`in`

import java.util.concurrent.TimeUnit
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.random.Random

@Path("/availability")
class AvailabilityResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun productAvailability(@QueryParam("productId") productId: String): Response = when (productId) {
        "1234" -> Response.ok(AvailabilityDTO(productId, 10)).build()
        "5678" -> Response.ok(AvailabilityDTO(productId, 30)).build()
        "91011" -> {
            TimeUnit.MILLISECONDS.sleep(Random.nextLong(500, 1500))
            Response.ok(AvailabilityDTO(productId, 50)).build()
        }
        "121314" -> {
            TimeUnit.MILLISECONDS.sleep(Random.nextLong(500, 1500))
            Response.ok(AvailabilityDTO(productId, 70)).build()
        }
        "151617" -> {
            TimeUnit.SECONDS.sleep(2)
            Response.ok(AvailabilityDTO(productId, 90)).build()
        }
        else -> Response.status(Response.Status.NOT_FOUND).build()
    }

    data class AvailabilityDTO(val productId: String, val quantity: Int)
}
