package com.ujazdowski

import com.github.tototoshi.csv.{CSVFormat, QUOTE_NONE, Quoting}

package object linguisticmathematics {

  implicit object CustomCSVFormat extends CSVFormat {
    override val delimiter: Char = ';'
    override val quoteChar: Char = '"'
    override val escapeChar: Char = '\\'
    override val lineTerminator: String = "\r\n"
    override val quoting: Quoting = QUOTE_NONE
    override val treatEmptyLineAsNil: Boolean = false
  }

}
