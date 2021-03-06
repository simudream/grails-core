package grails.test.mixin

import grails.persistence.Entity

@TestFor(Book2)
@Mock([Author2, Book2])
class UnitTestEmbeddedPropertyQuery {
    void setUp() {
        def author = new Author2(name: 'George')

        def book = new Book2(
            name: 'Game of Thrones',
            publishPeriod: new Period(
                startDate: new Date(2012, 1, 1),
                endDate: new Date(2013, 1, 1)
            )
        )

        author.addToBooks(book)
        author.save(flush: true, failOnError: true)
    }

    void testQueryEmbedded() {

        assert Book2.withCriteria {
            gt 'publishPeriod.startDate', new Date(2011, 1, 1)
        }.size() == 1

        assert Book2.withCriteria {
            ge 'publishPeriod.startDate', new Date(2012, 1, 1)
        }.size() == 1

        assert Book2.withCriteria {
            lt 'publishPeriod.startDate', new Date(2014, 1, 1)
        }.size() == 1

        assert Book2.withCriteria {
            le 'publishPeriod.startDate', new Date(2012, 1, 1)
        }.size() == 1

        assert Book2.withCriteria {
            eq 'publishPeriod.startDate', new Date(2012, 1, 1)
        }.size() == 1

        assert Book2.withCriteria {
            ne 'publishPeriod.startDate', new Date(2017, 1, 1)
        }.size() == 1

        assert Book2.withCriteria {
            isNotNull 'publishPeriod.startDate'
        }.size() == 1
    }

    void testAssociated() {
        assert Author2.withCriteria {
            books {
                gt 'publishPeriod.startDate', new Date(2011, 1, 1)
            }
        }.size() == 1

        assert Author2.withCriteria {
            books {
                ge 'publishPeriod.startDate', new Date(2012, 1, 1)
            }
        }.size() == 1

        assert Author2.withCriteria {
            books {
                lt 'publishPeriod.startDate', new Date(2014, 1, 1)
            }
        }.size() == 1

        assert Author2.withCriteria {
            books {
                le 'publishPeriod.startDate', new Date(2012, 1, 1)
            }
        }.size() == 1

        assert Author2.withCriteria {
            books {
                eq 'publishPeriod.startDate', new Date(2012, 1, 1)
            }
        }.size() == 1

        assert Author2.withCriteria {
            books {
                ne 'publishPeriod.startDate', new Date(2017, 1, 1)
            }
        }.size() == 1

        assert Author2.withCriteria {
            books {
                isNotNull 'publishPeriod.startDate'
            }
        }.size() == 1
    }

    void testQueryToOne() {
        assert Book2.withCriteria {
            gt 'publishPeriod.startDate', new Date(2011, 1, 1)
            author {
                eq 'name', 'George'
            }
        }.size() == 1
    }
}

@Entity
class Book2 {
    String name
    Period publishPeriod

    static belongsTo = [author: Author2]
    static embedded = ['publishPeriod']
}

@Entity
class Author2 {
    String name
    static hasMany = [books: Book2]
}

class Period {
    Date startDate
    Date endDate
}
