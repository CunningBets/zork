
.PHONY: build test

JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home
READER=ACS ACR1555 1S CL Reader(1)

default: uninstall install

uninstall:
	~/bin/gp --reader "$(READER)" -d --uninstall build/zork.cap

install: build
	~/bin/gp --reader "$(READER)" -d -install build/zork.cap

build:
	JAVA_HOME="$(JAVA_HOME)" ant

clean:
	JAVA_HOME="$(JAVA_HOME)" ant clean

test:
	JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-18.jdk/Contents/Home ./gradlew test
