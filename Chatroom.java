package com.api.whatsapp.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToMany
    private Set<User> members = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "chatroom_profiles",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    private Set<Profile> members = new HashSet<>();

    // Getters and setters

    public void addMember(Profile profile) {
        members.add(profile);
        profile.getChatrooms().add(this);
    }

    public void removeMember(Profile profile) {
        members.remove(profile);
        profile.getChatrooms().before(this);
    }
}