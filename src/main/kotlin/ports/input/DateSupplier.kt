package ports.input

interface DateSupplier {
  fun currentTimeMillis(): Long
}
