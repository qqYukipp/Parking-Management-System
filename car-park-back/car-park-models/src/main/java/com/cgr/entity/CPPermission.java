package com.cgr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("cp_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CPPermission implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String permName;
}
