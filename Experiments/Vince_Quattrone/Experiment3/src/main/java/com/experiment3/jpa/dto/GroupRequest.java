package com.experiment3.jpa.dto;

import com.experiment3.jpa.entity.Groupies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GroupRequest {

    private Groupies group;

}
