package app

import adapters.adapterModule
import adapters.envModule
import core.coreModule
import org.koin.core.context.GlobalContext.startKoin

fun main(args: Array<String>) {
  startKoin {
    modules(
      coreModule,
      adapterModule,
      envModule
    )
  }
}
