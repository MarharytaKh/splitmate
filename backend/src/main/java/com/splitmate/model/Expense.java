package com.splitmate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    // changed from double to long, now everything is in grosze (1 zl = 100)
    // double gave me 33.333333 and sums that were off by 0.01
    @Column(nullable = false)
    private long amountCents;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paid_by_id")
    private Member paidBy;

    @ManyToMany
    @JoinTable(
            name = "expense_participants",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @OrderBy("id")
    private List<Member> participants = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Expense() {
    }

    public Expense(String description, long amountCents, Member paidBy, List<Member> participants) {
        this.description = description;
        this.amountCents = amountCents;
        this.paidBy = paidBy;
        this.participants = new ArrayList<>(participants);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(long amountCents) {
        this.amountCents = amountCents;
    }

    public Member getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Member paidBy) {
        this.paidBy = paidBy;
    }

    public List<Member> getParticipants() {
        return participants;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
