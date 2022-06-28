.PHONY: clean
clean:
	@rm -rf out

.PHONY: build
build:
	@javac src/main/java/com/availity/*.java -d out
	@cp src/main/resources/data.csv out/

.PHONY: run
run:
	@java -classpath ./out com.availity.Ingester
