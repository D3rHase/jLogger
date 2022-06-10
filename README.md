
# jLogger - v1.0
[![](https://jitpack.io/v/D3rHase/jLogger.svg)](https://jitpack.io/#D3rHase/jLogger)  
A lightweight easy-to-use java logging libary.

## Usage
**1. Step**: Add the dependency to your `pom.xml`
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
```xml
<dependency>
  <groupId>com.github.D3rHase</groupId>
  <artifactId>jLogger</artifactId>
  <version>version</version>
</dependency>
```
**2. Step**: Enjoy your new logger:
```java
package com.example;
  
import de.d3rhase.interfaces.Logger;  
import de.d3rhase.txtlogger.TxtLogger;  
  
public class Class {  
    public Logger logger;  
  
    public Class() {
	    this.logger = new Logger(<String:logTitle>, <boolean:deleteExistingLogs>, <String:logDir>);
	    this.logger.error(<String:module>, <String:text>, <boolean:print|default:true>);
	}
}
```

## Examples

### 1. TXT-Logger:
```java
public Class() {
	this.logger = new TxtLogger(<String:logTitle>, <boolean:deleteExistingLogs|default:true>, <String:logDir|default:"logs">);
	this.logger.ok(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.info(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.debug(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.warning(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.error(<String:module>, <String:text>, <boolean:print|default:true>);
	}
}
```

#### Terminal:
```
OK - 2022-06-10__22-48-26.3956393 - MODULE - text
INFO - 2022-06-10__22-48-26.3996432 - MODULE - text
DEBUG - 2022-06-10__22-48-26.4006454 - MODULE - text
WARNING - 2022-06-10__22-48-26.4016452 - MODULE - text
ERROR - 2022-06-10__22-48-26.4026457 - MODULE - text
```
#### Log-file (logTitle_log-2022-06-10__22-48-26.33.txt):
```
---- LOG - logTitle - Date: 2022-06-10__22-48-26.33 ----  


OK - 2022-06-10__22-48-26.3956393 - MODULE - text
INFO - 2022-06-10__22-48-26.3996432 - MODULE - text
DEBUG - 2022-06-10__22-48-26.4006454 - MODULE - text
WARNING - 2022-06-10__22-48-26.4016452 - MODULE - text
ERROR - 2022-06-10__22-48-26.4026457 - MODULE - text
```

### 2. Json-Logger:
```java
public Class() {
	this.logger = new TxtLogger(<String:logTitle>, <boolean:deleteExistingLogs|default:true>, <String:logDir|default:"logs">);
	this.logger.ok(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.info(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.debug(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.warning(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.error(<String:module>, <String:text>, <boolean:print|default:true>);
	}
}
```

#### Terminal:
```
OK - 2022-06-10__23-00-12.8032834 - MODULE - text
INFO - 2022-06-10__23-00-12.8072867 - MODULE - text
DEBUG - 2022-06-10__23-00-12.8082885 - MODULE - text
WARNING - 2022-06-10__23-00-12.8082885 - MODULE - text
ERROR - 2022-06-10__23-00-12.8092887 - MODULE - text
```
#### Log-file (logTitle_log-2022-06-10__23-00-12.74.json):
```json
[  
  {  
      "date": "2022-06-10__23-00-12.74",  
      "type": "JsonLOG",  
      "title": "logTitle"  
  },  
  {  
      "module": "MODULE",  
      "time": "2022-06-10__23-00-12.8032834",  
      "text": "text",  
      "type": "OK"  
  },  
  {  
      "module": "MODULE",  
      "time": "2022-06-10__23-00-12.806286",  
      "text": "text",  
      "type": "INFO"  
  },  
  {  
      "module": "MODULE",  
      "time": "2022-06-10__23-00-12.8072867",  
      "text": "text",  
      "type": "DEBUG"  
  },  
  {  
      "module": "MODULE",  
      "time": "2022-06-10__23-00-12.8082885",  
      "text": "text",  
      "type": "WARNING"  
  },  
  {  
      "module": "MODULE",  
      "time": "2022-06-10__23-00-12.8092887",  
      "text": "text",  
      "type": "ERROR"  
  }  
]
```

### 3. TxtVLogger / JsonVLogger:
```java
public Class() {
	this.logger = new TxtVLogger(<String:logTitle>, <boolean:deleteExistingLogs|default:true>, <String:logDir|default:"logs">);
	
	this.logger = new JsonVLogger(<String:logTitle>, <boolean:deleteExistingLogs|default:true>, <String:logDir|default:"logs">);
	
	this.logger.ok(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.info(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.debug(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.warning(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.error(<String:module>, <String:text>, <boolean:print|default:true>);
	
	logger.saveLog();
	}
}
```

#### Terminal:
[Same as the "normal" logger](https://github.com/D3rHase/jLogger#terminal-1)
#### Log-file:
Empty until `logger.savelog();` ist called.

### 4. TxtVErrorLogger / JsonVErrorLogger:
```java
public Class() {
	this.logger = new TxtVErrorLogger(<String:logTitle>, <boolean:deleteExistingLogs|default:true>, <String:logDir|default:"logs">);
	
	this.logger = new JsonVErrorLogger(<String:logTitle>, <boolean:deleteExistingLogs|default:true>, <String:logDir|default:"logs">);
	
	this.logger.ok(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.info(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.debug(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.warning(<String:module>, <String:text>, <boolean:print|default:true>);
	this.logger.error(<String:module>, <String:text>, <boolean:print|default:true>);
	
	logger.saveLog();
	}
}
```

#### Terminal:
[Same as the "normal" logger](https://github.com/D3rHase/jLogger#terminal-1)
#### Log-file:
[Empty](https://github.com/D3rHase/jLogger#log-file) until `logger.savelog();` ist called.

**OR** an error is added to the log.
