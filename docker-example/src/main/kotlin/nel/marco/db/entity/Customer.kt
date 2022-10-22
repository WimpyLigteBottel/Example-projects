package nel.marco.db.entity

import javax.persistence.*

@Entity
@Table
class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null

    @Column
    var name:String? = null


}