const mongoose = require('mongoose')
const mgSchema = mongoose.Schema

const postSchema = new mgSchema({
    comment: String,
    userID: ObjectId,
})

module.exports = mongoose.model('Post', postSchema)