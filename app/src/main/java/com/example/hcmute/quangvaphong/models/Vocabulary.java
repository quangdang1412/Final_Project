package com.example.hcmute.quangvaphong.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor // Đảm bảo có constructor không tham số
@AllArgsConstructor
@SuperBuilder
public class Vocabulary {
    protected String word;
    protected String pronunciation;

    protected String meaning;
    protected Boolean isSave;
}
