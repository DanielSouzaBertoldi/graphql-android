const graphql = require('graphql')
const _ = require('lodash');

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
                return _.filter(postsData, {userID: parent.id})
            }
        },
        hobbies: {
            type: GraphQLList(HobbyType),
            resolve(parent) {
                return _.filter(hobbiesData, {userID: parent.id})
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
                return _.find(usersData, {id: parent.userID})
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
                return _.find(usersData, {id: parent.userID})
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
                return _.find(usersData, {id: args.id}) //_ = lodash doing its work
            }
        },
        hobby: {
            type: HobbyType,
            args: {
                id: {type: GraphQLID}
            },
            resolve(parent, args) {
                return _.find(hobbiesData, {id: args.id})
            }
        },
        post: {
            type: PostType,
            args: {
                id: {type: GraphQLID}
            },
            resolve(parent, args) {
                return _.find(postsData, {id: args.id})
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
                // id: {type: GraphQLID}
                name: {type: GraphQLNonNull(GraphQLString)},
                age: {type: GraphQLNonNull(GraphQLInt)},
                profession: {type: GraphQLString}
            },
            resolve(parent, args) {
                let user = {
                    name: args.name,
                    age: args.age,
                    profession: args.profession
                }
                return user
            }
        },
        CreatePost: {
            type: PostType,
            args: {
                comment: {type: GraphQLNonNull(GraphQLString)},
                userID: {type: GraphQLNonNull(GraphQLID)}
            },
            resolve(parent, args) {
                let post = {
                    userID: args.userID,
                    comment: args.comment
                }
                return post
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
                let hobby = {
                    title: args.title,
                    description: args.description,
                    userID: args.userID
                }
                return hobby;
            }
        }
    }
})

//Since we created this file in another folder, we need to export it
module.exports = new GraphQLSchema({
    query: RootQuery,
    mutation: Mutation
})