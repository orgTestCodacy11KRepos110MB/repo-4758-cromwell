package cloud.nio.impl.ftp

import common.assertion.CromwellTimeoutSpec
import common.mock.MockSugar
import org.apache.commons.net.ftp.FTPClient
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration._

class FtpClientPoolSpec extends AnyFlatSpec with CromwellTimeoutSpec with Matchers with MockSugar {

  behavior of "FtpClientPoolSpec"

  it should "logout and disconnect when clients are disposed" in {
    val client = mock[FTPClient]
    var loggedOut: Boolean = false
    var disconnected: Boolean = false
    client.isConnected.returns(true)
    client.logout().responds(_ => {
      loggedOut = true
      true
    })
    client.disconnect().responds(_ => {
      disconnected = true
    })

    val clientPool = new FtpClientPool(1, 10.minutes, () => { client })
    clientPool.acquire().invalidate()

    loggedOut shouldBe true
    disconnected shouldBe true
  }
}
