const graphql = require('graphql')
const _ = require('lodash')

let foodData = [
    {id: 1, name: 'Lasagna', recipe: 'Do this then that then put it in the oven'},
    {id: 2, name: 'Fishsticks', recipe: 'If you like it, then you\'re a gay fish.'},
    {id: 3, name: 'Pancakes', recipe: 'If you stop to think about, it\'s just a thin, tasteless cake.'},
    {id: 4, name: 'Cereal', recipe: 'The universal "I\'m not in the mood to cook." recipe.'},
    {id: 5, name: 'Hashbrowns', recipe: 'Just a potato and an oil and you\'re all set.'}
]

const {
    GraphQLSchema,
    GraphQLObjectType,
    GraphQLList,
    GraphQLNonNull,
    GraphQLString,
    GraphQLInt,
    GraphQLFloat,
    GraphQLBoolean,
    GraphQLID
} = graphql

//Scalar types
//String - Int - Float - Boolean - ID
const Person = new GraphQLObjectType({
    name: 'Person',
    description: 'Represents a Person type',
    fields: () => ({
        id: {type: GraphQLNonNull(GraphQLID)},
        name: {type: GraphQLNonNull(GraphQLString)},
        age: {type: GraphQLNonNull(GraphQLInt)},
        isMarried: {type: GraphQLBoolean},
        gpa: {type: GraphQLFloat},
        favoriteFoods: {type: GraphQLList(Food)},
        justAType: {
            type: Person,
            resolve(parent) {
                return parent
            }
        }
    })
})

const Food = new GraphQLObjectType({
    name: 'Food',
    description: 'Favorite food(s) of a person',
    fields: () => ({
        id: {type: GraphQLNonNull(GraphQLID)},
        name: {type: GraphQLNonNull(GraphQLString)},
        recipe: {type: GraphQLString}
    })
})

//Root Query
const RootQuery = new GraphQLObjectType({
    name: 'RootQueryType',
    description: 'Root Query',
    fields: {
        person: {
            type: Person,
            args: {
                id: {type: GraphQLID},
                name: {type: GraphQLString}
            },
            resolve(parent) {
                let person = {
                    name: 'Daniel',
                    age: 23,
                    isMarried: false,
                    favoriteFoods: [1, 2, 3]
                }

                foodIds = person.favoriteFoods
                for (var i = 0; i < foodIds.length; i++) {
                    person.favoriteFoods.push(_.find(foodData, {id: foodIds[i]}))
                    person.favoriteFoods.shift()
                }

                return person
            }
        }
    }
})

module.exports = new GraphQLSchema({
    query: RootQuery
})