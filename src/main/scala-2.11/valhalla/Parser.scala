package valhalla

import java.io._

import com.hp.hpl.jena.sparql.procedure.library.debug
import org.apache.jena.rdf.model.{ModelFactory, Statement, StmtIterator}
import org.apache.log4j.{Level, LogManager}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
  * Provides format transformation from RDF/XML to N-TRIPLE
  *
  * Created by Mircea 
  */


  object Parser {
    val options = mutable.HashMap.empty[String, ArrayBuffer[String]]

  /**
    *
    * Entry point
    * @param args
    */
    def main(args: Array[String]) {
      LogManager.getRootLogger().setLevel(Level.OFF)
      optionParser(args)
      println("Format transformation starting..")
      startparsing
    }

  /**
    * Perform batch processing of multiple files
    */
    def startparsing(): Unit = {
      if (!options.contains("help") && options.contains("input-file") && options.contains("output-file")) {
        if (options("input-file").nonEmpty && options("output-file").length == 1) {
          val bw = new BufferedWriter(new FileWriter(options("output-file")(0)))
          options("input-file") foreach (x => runner(x, bw))
          bw.close()
          println(s"All processing done!")
          println(s"Your n-triple file is: ${options("output-file")(0)} ")
          } else {
            showOptions()
          }
        }
        else {
          showOptions()
        }
      }

  /**
    * Help menu for command line arguments
    */
    def showOptions(): Unit = {
      println("Usage:")
      println("\tjava -jar Parser.jar --input-file [<fileNameWithPath>..] --output-file [<filenameWithPath] --help ")
      println("\n\tParameters:")
      println("\t\t--input-file: files you wish to process   **REQUIRED")
      println("\t\t--output-file: file where to store the output **REQUIRED")
      println("\t\t--help: displays this message")
    }

  /**
    * Parser for 1* file into n-triple format.
    * <strong>
    * Input file(s) must be in RDF/XML format
    * </strong>
    *
    * @param filename       input file
    * @param bufferedWriter writer to the output file
    */
    def runner(filename: String, bufferedWriter: BufferedWriter): Unit = {
      println(s"Currently processing $filename")
      val model = ModelFactory.createDefaultModel()
      var t0=System.nanoTime()
      model.read(new FileReader(filename), "RDF/XML")
      println(s"Model loading time: ${(System.nanoTime-t0)/1000000000.0}")
      t0=System.nanoTime()
      model.write(bufferedWriter, "N-TRIPLE")
      println(s"Model loading time: ${(System.nanoTime-t0)/1000000000.0}")
      println(s"Processing for  $filename done")
    }


  /**
    * Parses and validates command line arguments
    * @param args input & output file destination
    */
    def optionParser(args: Array[String]) = {
      val token = "--"
      var currentToken = ""
      val availableParams = Array("input-file", "output-file", "help", "fusion")

      for (item <- args) {
      //only param token no identifier
      if (item.contains(token)) {
        //param not empty && matched in available options
        if (item.length > 3) {
          if (availableParams contains item.substring(token.length)) currentToken = item.substring(2)
          else {
            println(s"ERROR: unrecognized param $item, \nExiting....")
            System.exit(1)
          }
        }
        else currentToken = "" //discard empty token e.g "-*-"
      }
      //parameter value for token
      else if (!currentToken.isEmpty) {
        try {
          options(currentToken) += item
          } catch {
            case e: Exception => options(currentToken) = ArrayBuffer(item)
          }
        }
      }
    }
  }



