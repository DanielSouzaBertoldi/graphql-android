const express = require('express')
const graphql = require('express-graphql')

const schema = require('./schema/schema')
const app = express()

app.use('/graphql', graphql({
    graphiql: true,
    schema: schema //since both have the same name, there's no need to specify the schema name after the :
}));

app.listen('6050', () => {
    console.log("listening right now my youngling 6050")
});