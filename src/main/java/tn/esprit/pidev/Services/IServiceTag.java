package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Tag;

import java.util.List;

public interface IServiceTag {
    public Tag addTag(Tag tag);
    public List<Tag> getAllTags();
    public void deleteTag(String tagId);
    public List<String> getAllTagNames();
}
