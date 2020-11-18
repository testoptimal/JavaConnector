# JavaConnector
API to access [TestOptimal](https://testoptimal.com) Server for execution.

[TestOptimal](https://testoptimal.com) is a [Model-Based Testing (MBT)](https://en.wikipedia.org/wiki/Model-based_testing) tool, which generates test cases from a state diagram or from a set of variables.

You can use this JavaConnector to perform online and offline MBT in your Java IDE/Studio.  Refer to one of the following use cases to evaluate fit to your testing needs.
 
## Use Case 1 - State Diagram Test Generation
>

	// create model
	Model model = new Model("My First Model");
	State start = model.addStateInitial("Start");
	State s2 = model.addState("State 2");
	State s3 = model.addState("State 3");
	State end = model.addStateFinal("End");
	start.addTrans("trans_12", s2).addTrans("trans_13", s3);
	s2.addTrans("trans_23", s3).addTrans("trans_2end", end);
	s3.addTrans("trans_3end", end);
	
	// generate test cases from the model
	Server svr = new Server(Protocol.http, "localhost", 8888, "lin@to.com", "test"));
	ModelAPI api = svr.getModelAPI();
	api.upload(model);
	RunResult result = api.genPaths(model.getName(), MbtMode.Optimal);
	System.out.println(result);
	
	// Now you have the test cases in result, do your test automation	
	...
	
Use one of the following sequencers (MbtMode) to achieve different test coverage: 
- Optimal
- Priority
- Pairwise
- Random
Refer to [MBT Sequencers](https://testoptimal.com/v6/wiki/doku.php?id=sequencers) for more details.

You can also generate model graph and test case graphs (GraphType):
- [model](https://testoptimal.com/v6/wiki/lib/exe/fetch.php?media=wiki:overview:graph_model.png)
- [coverage](https://testoptimal.com/v6/wiki/lib/exe/fetch.php?media=wiki:overview:graph_coverage.png)
- [sequence](https://testoptimal.com/v6/wiki/lib/exe/fetch.php?media=wiki:overview:graph_traversal.png)
- [msc](https://testoptimal.com/v6/wiki/lib/exe/fetch.php?media=wiki:overview:graph_msc.png)

>
	ModelAPI apiModel = svr.getModelAPI();
	apiModel.getGraphModel(model.getName(), "model.png");
	apiModel.getGraphSequence(model.getName(), "sequence.png");
	apiModel.getGraphCoverage(model.getName(), "coverage.png");
	apiModel.getGraphMSC(model.getName(), "MSC.png");

## Use Case 2 - State Diagram Automation
This use case illustrates how you can have your java code called remotely from your model running.  This differs from Use Case 1 above that, instead of looping through test cases generated and execute automation, the test cases are executed on TestOptimal server and your java code is told exactly what action to perform. 

This use case allows you to leverage the model execution stats collection.

>
	Server svr = new Server(Protocol.http, "localhost", 8888, "lin@to.com", "test"));
	AgentAPI api = svr.getAgentPI();
	api.startModel(modelName);
	api.regAgent(modelName);
	String cmd = api.getNextCmd();
	while (cmd != null && !cmd.equals("")) {
		System.out.println("Next Cmd: " + cmd);
		api.setResult(true, "Echo " + cmd);
		cmd = api.getNextCmd();
	}
	System.out.println ("Agent is done");
	RunResult result = api.getModelStats(modelName);
	System.out.println("results: " + Gson.toJson(result));


## Use Case 3 - Combinatorial Testing
>
	// create dataset and its fields
	DataSet ds = new DataSet("ds1");
	ds.addField ("F1", "1,2,3".split(","));
	ds.addField ("F2", "a,b,c".split(","));
	
	// generate test cases 
	Server svr = new Server(Protocol.http, "localhost", 8888, "lin@to.com", "test");
	DataSetAPI api = svr.getDataSetAPI ();
	api.upload(this.ds);
	RunResult results = api.generate(ds.getName(), Constants.Algorithm.pairWise);
	System.out.println(results);
	
	// Now you have the test cases in result, do your test automation	
	...

You may choose one of the following combinatorial algorithms to genreate test cases:
- pairWise
- threeWise
- fourWise
- fiveWise
-_sixWise
- mixed (used when specifying different strength for subset of variables)
Refer to [DataDesigner](https://testoptimal.com/v6/wiki/doku.php?id=data_design_ide) for more details.

## Setup for Eclipse and Other Java IDE
Download [TestOptimal JavaConnector zip](https://testoptimal.com/downloads/Rel-6.0/TestOptimal-JavaConnector.zip) and unzip it to your local directory. You should find these files:

- LICENSE
- README.md
- TestOptimal-JavaConnector-[version].jar
- TestOptimal-JavaConnector-[version]-jar-with-dependencies.jar

Add one of the jar file to your project class path for your project in Eclipse.  Other Java IDE works the same way.

## License
[Apache License Version 2](http://www.apache.org/licenses/LICENSE-2.0)

## Support / Help
See src/test/java/ folder for examples.

Post your questions to [TestOptimal Community Forum](https://testoptimal.com/forum)



