const express = require('express')
const graphQLHTTP = require('express-graphql')

const conn = require('./conn/conn')
const mongoose = require('mongoose')

//Removing deprecations with the following flags
mongoose.connect(conn.uri, { useNewUrlParser: true,  useUnifiedTopology: true})
mongoose.connection.once('open', () => {
    console.log('Connected.')
})

const schema = require('./schema/schema')
const testSchema = require('./schema/types_schema.js') //Just for testing and getting the hang of types
const app = express()

app.use('/graphql', graphQLHTTP({
    graphiql: true,
    schema: schema //since both have the same name, there's no need to specify the schema name after the :
}));

//Testing types
app.use('/graphql-test', graphQLHTTP({
    graphiql: true,
    schema: testSchema
}))

app.listen('6050', () => {
    console.log("listening right now my youngling 6050")
});