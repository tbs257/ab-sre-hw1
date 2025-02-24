package client

sealed trait CircuitBreakerState

object CircuitBreakerState {
  case object Closed extends CircuitBreakerState
  case object Open extends CircuitBreakerState
  case object HalfOpen extends CircuitBreakerState
}