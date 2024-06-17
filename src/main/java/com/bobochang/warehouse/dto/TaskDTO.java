package com.bobochang.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskDTO {
    private String id;
    private String name;
    private String assignee;
    // Other relevant fields
    private String taskAssignee;
    private String taskId;
    private String taskName;
    // Getters and setters
}