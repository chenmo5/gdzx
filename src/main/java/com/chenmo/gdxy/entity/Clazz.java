package com.chenmo.gdxy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenmo
 * @since 2022-05-14
 */
@TableName("tb_clazz")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Clazz extends Model<Clazz> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer number;

    private String introducation;

    private String headmaster;

    private String email;

    private String telephone;

    private String gradeName;


}
