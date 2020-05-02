const mongoose = require('mongoose')
const mgSchema = mongoose.Schema

const hobbySchema = new mgSchema({
    title: String,
    description: String,
    userID: mongoose.ObjectId
})

module.exports = mongoose.model('Hobby', hobbySchema)