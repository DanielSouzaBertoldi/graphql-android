// const MongoClient = require('mongodb').MongoClient;
const uri = "<your_mongo_connection>";
// const client = new MongoClient(uri, { useNewUrlParser: true });
// client.connect(err => {
//   const collection = client.db("test").collection("devices");
//   // perform actions on the collection object
//   client.close();
// });

module.exports = { uri: uri }