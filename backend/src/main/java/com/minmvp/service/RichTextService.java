package com.minmvp.service;

import com.minmvp.dto.RichTextDTO;
import com.minmvp.entity.RichText;

import java.util.List;

public interface RichTextService {
    RichText save(RichTextDTO dto, Long creatorId);
    RichText getById(Long id);
    List<RichText> list();
}
