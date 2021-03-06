package com.example.learnterm

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    val fullName: String,
    val email: String,
    val accountName: String,
) {
    // @Column(nullable = false, updatable = false)
    @Id
    @GeneratedValue
    val userID: Int = 0

    @JoinTable(name = "users_completedLevels",
    joinColumns = [JoinColumn(name = "completedBy_userID", referencedColumnName = "userID")],
    inverseJoinColumns = [JoinColumn(name = "completedLevels_id", referencedColumnName = "id")])
    @ManyToMany(fetch = FetchType.EAGER)
    val completedLevels: MutableSet<Level> = mutableSetOf()
        // private set

    @Override
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return userID == other.userID
    }

    @Override
    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(userID = $userID , fullName = $fullName , email = $email , accountName = $accountName)"
    }
}
