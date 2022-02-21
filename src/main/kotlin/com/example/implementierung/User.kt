package com.example.implementierung

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    val fullName: String,
    val email: String,
    val accountName: String,
) {
    @Column(nullable = false, updatable = false)
    @Id
    @GeneratedValue
    val userID: Int = 0

    @ManyToMany
    val completedLevels: MutableSet<Level> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return userID == other.userID
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(userID = $userID , fullName = $fullName , email = $email , accountName = $accountName)"
    }
}
