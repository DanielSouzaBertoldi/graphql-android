const mongoose = require('mongoose')
const mgSchema = mongoose.Schema

//There's no need to specify ID
//since every model has to have an ID
const userSchema = new mgSchema({
    name: String,
    age: Number,
    profession: String
})

module.exports = mongoose.model('User', userSchema)