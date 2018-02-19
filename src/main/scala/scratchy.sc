

val string = """{
               |  "success" : true,
               |  "publicId" : "dbb05f1f-bae4-4510-9925-7a280a7c44d4"
               |}""".stripMargin

val extractor = """"publicId" *: *"(.+)"""".r

extractor.findFirstMatchIn(string).map(_.group(1))
