const mongoose = require('mongoose')
const mgSchema = mongoose.Schema

const hobbySchema = new mgSchema({
    title: String,
    description: String,
})

module.exports = mongoose.model('Hobby', hobbySchema)