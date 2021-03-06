package com.ujazdowski.linguisticmathematics

import com.github.tototoshi.csv.CSVReader

import scala.collection.mutable.ListBuffer
import scala.io.{Source, StdIn}

object Main extends App {

  private val initialState = "q0"

  private val states = loadStates
  private val transitions = loadTransitions
  private val transitionHistory = new ListBuffer[State]()
  private var currentState: State = _

  startFSM()
  showHistory()

  private def startFSM(): Unit = {
    setState(findState(initialState))

    while (true) {
      println(s"${currentState.name}: ${currentState.description}. ${currentState.action}")
      if (hasTransitions) {
        val token = StdIn.readLine().toUpperCase
        setState(findNextState(token))
      } else {
        return
      }
    }
  }

  private def showHistory(): Unit = {
    println("Transition history")
    val history = transitionHistory.map(_.name).mkString(" -> ")
    println(history)
  }

  private def setState(state: State): Unit = {
    currentState = state
    transitionHistory.addOne(state)
  }

  private def findNextState(token: String): State = findState(
    transitions.find(transition => transition.formState == currentState.name && transition.token == token)
      .getOrElse(throw TransitionNotExists(currentState.name, token))
      .toState
  )

  private def hasTransitions: Boolean = {
    transitions.exists(transition => transition.formState == currentState.name)
  }

  private def findState(name: String): State = {
    states.find(state => state.name == name).getOrElse(throw StateNotExists(name))
  }

  private def loadStates: List[State] = loadFile("states.csv").map {
    case state :: description :: action :: Nil => State(state, description, action)
  }

  private def loadTransitions: List[Transition] = loadFile("transitions.csv").map {
    case from :: token :: to :: Nil => Transition(token, from, to)
  }

  private def loadFile(fileName: String): List[List[String]] = {
    val statesCsv = CSVReader.open(Source.fromResource(fileName))
    val allLines = statesCsv.all()
    statesCsv.close()
    allLines
  }

  case class TransitionNotExists(from: String, token: String) extends RuntimeException(s"Transition does not exists! From state: $from, with token: $token")

  case class StateNotExists(name: String) extends RuntimeException(s"Cannot find state with name: $name")

}
