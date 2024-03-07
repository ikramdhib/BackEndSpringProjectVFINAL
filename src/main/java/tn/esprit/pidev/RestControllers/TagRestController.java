package tn.esprit.pidev.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Services.IServiceTag;
import tn.esprit.pidev.entities.Tag;

import java.util.List;
@RestController
@AllArgsConstructor
public class TagRestController {
    private IServiceTag iServiceTag;
    @PostMapping("/addTag")
    public Tag addTag(@RequestBody Tag tag) {
        return iServiceTag.addTag(tag);
    }

    @DeleteMapping("/deleteTag/{tagId}")
    public void deleteTag(@PathVariable String tagId) {
        iServiceTag.deleteTag(tagId);
    }
    @GetMapping("/getAllTags")
    public List<String> getAllTags() {
        return iServiceTag.getAllTagNames();
    }
}
