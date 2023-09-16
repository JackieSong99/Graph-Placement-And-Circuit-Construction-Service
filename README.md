# Graph Placement And Circuit Construction Service
This is a service that includes Boolean circuit construction service and heterogeneous cloud environment resources placement service. The logic part is in Java and 
the framework is in Django. 

# Package the service
Step 1: `docker build -t twoservices`.
Step 2: `docker run -d -p 8999:8999 twoservices`.

# Test
Direct into the `jsonfile` folder. 
1. Run `curl -X POST http://localhost:8999/circuit-api/circuit-construction/test/ -H 'Content_Type: application/json' -d '@circuit_input_test6.json'` in terminal,
   it should return 'Circuit Construction test-API Hello'.

2. Run `curl -X POST http://localhost:8999/circuit-api/circuit-construction/construct/ -H 'Content_Type: application/json' -d '@circuit_input_test6.json'` in terminal,
   it should return the computed result for the content in file `circuit_input_test6.json`.

3. Run `curl -X POST http://localhost:8999/optimization-api/optimization/test/ -H 'Content_Type: application/json' -d '@input_graph.json'` in terminal,
   it should return `'Optimization API Hello'`.

4. Run `curl -X POST http://localhost:8999/optimization-api/optimization/optimize/ -H 'Content_Type: application/json' -d '@input_graph.json'` in terminal,
    it should return the computed result for the content in file `input_graph.json`.

You can provide your own test example as well!


The circuit library is not been presented since it was developed by my supervisor.
