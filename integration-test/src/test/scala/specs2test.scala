import org.specs2.mutable.Specification

/**
 * @author sofn 
 * @version 1.0 Created at: 2014-10-23 19:00
 */
class specs2test extends Specification {
  "HelloWorldUnitSpec" should {
    "contain 11 char" in {
      "Hello world" must have size 11
    }
    "start with 'Hello'" in {
      "Hello World" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }

  "contain 11 char" in {
    "Hello world" must have size 11
  }

  "my example test" ! e1
  "my example2 test" ! e2

  def e1 = {
    "Hello" must startWith("Hello")
  }

  def e2 = {
    "Hello" must have size 5 and startWith("Hello")
  }


}

