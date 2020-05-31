// const MongoClient = require('mongodb').MongoClient;
const uri = "mongodb+srv://graphql-user:3wdizRvbcqv9NA44@graphql-jgscl.gcp.mongodb.net/test?retryWrites=true&w=majority";
// const client = new MongoClient(uri, { useNewUrlParser: true });
// client.connect(err => {
//   const collection = client.db("test").collection("devices");
//   // perform actions on the collection object
//   client.close();
// });

module.exports = { uri: uri }