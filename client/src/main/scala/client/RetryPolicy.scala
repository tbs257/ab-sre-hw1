package client

import sttp.model.StatusCode

case class RetryPolicy(retryCount: Int, retryableCodes: Set[StatusCode])
