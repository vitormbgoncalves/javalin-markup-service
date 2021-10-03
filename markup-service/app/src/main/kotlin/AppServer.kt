import adapters.adapterModule
import core.coreModule
import org.koin.core.context.GlobalContext.startKoin

fun main(args: Array<String>) {
  startKoin {
    modules(
      coreModule,
      adapterModule
    )
  }

//  Runtime.getRuntime().addShutdownHook(Thread { println("In the middle of a shutdown") })
}
