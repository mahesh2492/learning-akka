package akka.examples.mailbox

import akka.actor.ActorSystem
import akka.dispatch.{PriorityGenerator, UnboundedStablePriorityMailbox}
import com.typesafe.config.Config

/*
Create a priority mail box which will process string message first, followed by int and long messages.

 */
class MyPriorityActorMailbox(settings: ActorSystem.Settings, config: Config) extends UnboundedStablePriorityMailbox(
  PriorityGenerator {

    case _: String => 0 //high priority
    case _: Int    => 1
    case _: Long   => 2
    case _         => 3 //low priority
  })
