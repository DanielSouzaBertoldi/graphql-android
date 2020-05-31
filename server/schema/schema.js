const graphql = require('graphql')
const _ = require('lodash')

const mongoose = require('mongoose')
const User = require('../model/User')
const Hobby = require('../model/Hobby')
const Post = require('../model/Post')

//Dummy data while we don't set up a database
let usersData = [
    {id: '1', name: 'Bond', age: 35, profession: 'WESTERN Spy'},
    {id: '12', name: 'Patrick', age: 25, profession: 'Krusty Crab Employee'},
    {id: '123', name: 'Morgan', age: 41, profession: 'Soldier Scientist'},
    {id: '1234', name: 'Espoleta', age: 3, profession: 'Dog'},
    {id: '12345', name: 'Android', age: 15, profession: 'Operational System'}
]

let hobbiesData = [
    {id: '1', title: 'Programming', description: 'Loves to code stuff and try to automate tasks.', userID: '12345'},
    {id: '2', title: 'Cooking', description: 'Trying out every new recipe that pops up in their YouTube feed.', userID: '12'},
    {id: '3', title: 'Painting', description: 'They\'re not Picasso\'s, but DevianArt loves them.', userID: '1'},
    {id: '4', title: 'Gaming', description: 'Why bother with reality when you can pass time in a virtual world?', userID: '1234'},
    {id: '5', title: 'Fishing', description: 'To find peace within, you need to have the pacience of a fisherman.', userID: '123'},
    {id: '6', title: 'Cleaning', description: 'Cleaning my kitchen feels like cleansing my soul.', userID: '1234'},
    {id: '7', title: 'Trekking', description: 'What is trekking? I don\'t know but I love it!', userID: '123'}
]

let postsData = [
    {id: '1', comment: 'This s*** is so sick!', userID: '1'},
    {id: '2', comment: 'Let\'s play some TF2!!!', userID: '123'},
    {id: '3', comment: 'This s*** is so whack.', userID:'12345'},
    {id: '4', comment: 'Does anyone remember Keyboard Cat?', userID: '12'},
    {id: '5', comment: 'I\'m more of a Nyan Cat person.', userID: '1234'},
    {id: '6', comment: 'Sometimes I dream about cheese', userID: '1'},
    {id: '7', comment: 'Smashing some headcrabs, hell yeah.', userID: '123'},
    {id: '8', comment: 'Remember when my name used to be candy names?', userID:'12345'},
    {id: '9', comment: 'NO!!! THIS IS PATRICK!!!!!!', userID: '12'},
    {id: '10', comment: 'woof', userID: '1234'}
]

// Importing objects and types that we need from
// graphql globally
const {
    GraphQLSchema,
    GraphQLObjectType,
    GraphQLID,
    GraphQLString,
    GraphQLInt,
    GraphQLList,
    GraphQLNonNull
} = graphql

let cleanFields = function(info) {
    //This line will remove all optional fields that don't have any value to them.
    for(let field in info) if(!info[field]) delete info[field]
    return info
}

//Types, aka 'Tables'
const UserType = new GraphQLObjectType({
    name: 'User',
    description: 'Returns info about a user.',
    fields: () => ({
        id: {type: GraphQLID},
        name: {type: GraphQLString},
        age: {type: GraphQLInt},
        profession: {type: GraphQLString},
        posts: {
            type: GraphQLList(PostType),
            resolve(parent) {
                return Post.find({userID: parent.id})
            }
        },
        hobbies: {
            type: GraphQLList(HobbyType),
            resolve(parent) {
                return Hobby.find({userID: parent.id})
            }
        }
    })
})

const HobbyType = new GraphQLObjectType({
    name: 'Hobby',
    description: 'Returns the title and description of a hobby.',
    fields: () => ({
        id: {type: GraphQLID},
        title: {type: GraphQLString},
        description: {type: GraphQLString},
        user: {
            type: UserType,
            resolve(parent) {
                return User.findById(parent.userID)
            }
        }
    })
})

const PostType = new GraphQLObjectType({
    name: 'Post',
    description: 'Returns the comments of a post.',
    fields: () => ({
        id: {type: GraphQLID},
        comment: {type: GraphQLString},
        userID: {type: GraphQLID},
        user: {
            type: UserType,
            resolve(parent) {
                return User.findById(parent.userID)
            }
        }
    })
})

//OBS: () => () it's a callback function

//RootQuery (path that allows us to traverse the schema)
const RootQuery = new GraphQLObjectType({
    name: 'RootQueryType',
    description: 'The RootQuery',
    fields: {
        user: {
            type: UserType,
            args: {
                id: {type: GraphQLID}
            },
            resolve(parent, args) {
                //Get and return data from the DB
                return User.findById(args.id)
            }
        },
        users: {
            type: GraphQLList(UserType),
            resolve(parent) {
                return User.find({})
            }
        },
        hobby: {
            type: HobbyType,
            args: {
                id: {type: GraphQLID}
            },
            resolve(parent, args) {
                return Hobby.findById(args.id)
            }
        },
        hobbies: {
            type: GraphQLList(HobbyType),
            resolve(parent) {
                return Hobby.find({})
            }
        },
        post: {
            type: PostType,
            args: {
                id: {type: GraphQLID}
            },
            resolve(parent, args) {
                return Post.findById(args.id)
            }
        },
        posts: {
            type: GraphQLList(PostType),
            resolve(parent) {
                return Post.find({})
            }
        }
    }
})

//Mutations
const Mutation = new GraphQLObjectType({
    name: 'Mutation',
    fields: {
        CreateUser: {
            type: UserType,
            args: {
                // id: {type: GraphQLID} -- We don't need to pass and ID since Mongo handles that for us
                name: {type: GraphQLNonNull(GraphQLString)},
                age: {type: GraphQLNonNull(GraphQLInt)},
                profession: {type: GraphQLString}
            },
            resolve(parent, args) {
                let user = new User({
                    name: args.name,
                    age: args.age,
                    profession: args.profession
                })
                // Saving in BD
                return user.save()
            }
        },
        UpdateUser: {
            type: UserType,
            args: {
                id: {type: GraphQLNonNull(GraphQLID)},
                name: {type: GraphQLNonNull(GraphQLString)},
                age: {type: GraphQLInt},
                profession: {type: GraphQLString}
            },
            resolve(parent, args) {
                let userData = {
                    name: args.name,
                    age: args.age,
                    profession: args.profession
                }

                userData = cleanFields(userData)
                
                //The flag "new" returns the document with the updated values,
                //not the old values
                return User.findByIdAndUpdate(args.id, {new: true})
            }
        },
        DeleteUser: {
            type: UserType,
            args: { id: {type: GraphQLNonNull(GraphQLID)} },
            resolve(parent, args) {
                deletedUser = User.findByIdAndDelete(args.id);

                //If we delete a user, then we need to delete all hobbies and posts
                //associated with them.
                //NOT WORKING, WHY???/????
                Hobby.deleteMany({userID: mongoose.ObjectId(args.id)})
                Post.deleteMany({userID: mongoose.ObjectId(args.id)})

                return deletedUser
            }
        },
        CreatePost: {
            type: PostType,
            args: {
                comment: {type: GraphQLNonNull(GraphQLString)},
                userID: {type: GraphQLNonNull(GraphQLID)}
            },
            resolve(parent, args) {
                let post = new Post({
                    userID: args.userID,
                    comment: args.comment
                })
                return post.save()
            }
        },
        UpdatePost: {
            type: PostType,
            args: {
                id: {type: GraphQLNonNull(GraphQLID)},
                comment: {type: GraphQLNonNull(GraphQLString)}
            },
            resolve(parent, args) {
                Post.findByIdAndUpdate(args.id, {comment: args.comment}, {new: true})
            }
        },
        DeletePost: {
            type: PostType,
            args: { id: {type: GraphQLNonNull(GraphQLID)} },
            resolve(parent, args) {
                return Post.findByIdAndDelete(args.id)
            }
        },
        CreateHobby: {
            type: HobbyType,
            args: {
                title: {type: GraphQLNonNull(GraphQLString)},
                description: {type: GraphQLString},
                userID: {type: GraphQLNonNull(GraphQLID)}
            },
            resolve(parent, args) {
                let hobby = new Hobby({
                    title: args.title,
                    description: args.description,
                    userID: args.userID
                })
                return hobby.save()
            }
        },
        UpdateHobby: {
            type: HobbyType,
            args: {
                id: {type: GraphQLNonNull(GraphQLString)},
                title: {type: GraphQLNonNull(GraphQLString)},
                description: {type: GraphQLString}
            },
            resolve(parent, args) {
                let hobbyData = {
                    title: args.title,
                    description: args.description
                }

                hobbyData = cleanFields(hobbyData)

                Hobby.findByIdAndUpdate(args.id, hobbyData, {new: true})
            }
        },
        DeleteHobby: {
            type: HobbyType,
            args: { id: {type: GraphQLNonNull(GraphQLID)} },
            resolve(parent, args) {
                Hobby.findByIdAndDelete(args.id)
            }
        }
    }
})

//Since we created this file in another folder, we need to export it
module.exports = new GraphQLSchema({
    query: RootQuery,
    mutation: Mutation
})