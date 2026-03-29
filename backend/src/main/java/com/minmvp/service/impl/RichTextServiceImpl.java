package com.minmvp.service.impl;

import com.minmvp.dto.RichTextDTO;
import com.minmvp.entity.RichText;
import com.minmvp.mapper.RichTextMapper;
import com.minmvp.service.RichTextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RichTextServiceImpl implements RichTextService {

    private static final Logger log = LoggerFactory.getLogger(RichTextServiceImpl.class);

    @Autowired
    private RichTextMapper richTextMapper;

    @Override
    public RichText save(RichTextDTO dto, Long creatorId) {
        RichText richText = new RichText();
        richText.setTitle(dto.getTitle());
        richText.setContent(dto.getContent());
        richText.setCreatorId(creatorId);
        richTextMapper.insert(richText);
        return richText;
    }

    @Override
    public RichText getById(Long id) {
        return richTextMapper.selectById(id);
    }

    @Override
    public List<RichText> list() {
        return richTextMapper.selectList(null);
    }
}
