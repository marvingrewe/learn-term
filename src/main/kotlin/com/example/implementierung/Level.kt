package com.example.implementierung

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "levels")
data class Level(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val levelID: Int,

    val name: String,

    @ManyToMany(mappedBy = "completedLevels")
    val completedBy: MutableSet<User>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Level

        return levelID == other.levelID
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(levelID = $levelID , name = $name )"
    }
}
