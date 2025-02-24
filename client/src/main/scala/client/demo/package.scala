package client

import com.comcast.ip4s.IpLiteralSyntax
import sttp.model.StatusCode

package object demo {
  val host    = ipv4"0.0.0.0"
  val portInt = 8083
  val path    = "hello"

  val retryableCodes: Set[StatusCode] = Set(
    StatusCode.InternalServerError,
    StatusCode.BadGateway,
    StatusCode.ServiceUnavailable,
    StatusCode.GatewayTimeout,
  )
}
