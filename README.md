# sale-point-system-JavaFx
JavaFx 17 , MySQL POS . It includes intruction to get the fat jar and build exe installer with jlink and jpackage.


Dont scare by the instruction:

Run this command to check the dependencies used in the fatJar
```
jdeps --module-path "install/windows/jmods" --add-modules=javafx.controls --list-deps "shade/betel.jar"
```


These are the necessary instruction to create a jre from javaFx jmods.
```
jlink --output install/windows/jdk20+fx  --module-path install/windows/jmods  --add-modules javafx.base,javafx.controls,
javafx.fxml,java.logging,java.datatransfer,java.desktop,java.logging,java.management,java.naming,java.rmi,java.scripting,
java.sql,java.transaction.xa,java.xml,
jdk.jsobject,jdk.unsupported,jdk.unsupported.desktop,jdk.xml.dom
```
For this project am using MySQL jdbc connector,
the   "--module-path" will let you add a jmods path, if you already had java SDK 11 or later  installed and   paths configured 
you can simply add the those to the paths listed in by jdeps command. Otherwise i recomment to download jmods from official JavaF website.
(https://gluonhq.com/products/javafx/)



Nos this last command will let you create the installer.
```
jpackage ^
  --input shade ^
  --name betel ^
  --main-jar betel.jar ^
  --main-class com.mycompany.sale_point.main ^
  --description "Java Fx Non Modular Example" ^
  --vendor "DIEGO" ^
  --icon install/assets/ico.ico ^
  --dest install/output ^
  --app-version 1.0 ^
  --runtime-image install/windows/jdk20+fx ^
  --resource-dir install/windows/resources ^
  --win-shortcut ^
  --win-dir-chooser ^
  --win-menu ^
  --win-menu-group "Diego Pos" ^
  --type exe

```



