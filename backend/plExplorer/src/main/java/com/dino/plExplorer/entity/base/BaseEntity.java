package com.dino.plExplorer.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof BaseEntity that)) return false;
        if(!(Hibernate.getClass(this).equals(Hibernate.getClass(o)))) return false;
        return id != null && id.equals(that.getId());


    }

    @Override
    public int hashCode(){
        //fiksni hash isti za sve objekte neke klase
        // jer se id mjenja iz null u neki int prilikom spremanja u bazu
        // a hashCode se ne smije mjenajti dok je objekt u Setu
        return getClass().hashCode();
    }

}
