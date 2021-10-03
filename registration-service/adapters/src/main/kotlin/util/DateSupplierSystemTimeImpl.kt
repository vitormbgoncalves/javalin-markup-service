package adapters.util

import ports.input.DateSupplier

class DateSupplierSystemTimeImpl : DateSupplier {

  override fun currentTimeMillis() = System.currentTimeMillis()
}
