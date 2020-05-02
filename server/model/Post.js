const mongoose = require('mongoose')
const mgSchema = mongoose.Schema

const postSchema = new mgSchema({
    comment: String,
    userID: mongoose.ObjectId
})

module.exports = mongoose.model('Post', postSchema)