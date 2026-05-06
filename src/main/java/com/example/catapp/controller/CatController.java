package com.example.catapp.controller;

import com.example.catapp.entity.Cat;
import com.example.catapp.entity.User;
import com.example.catapp.repository.CatRepository;
import com.example.catapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/cats")
public class CatController {

    private final CatRepository catRepository;
    private final UserRepository userRepository;

    private final Random rand = new Random();

    public CatController(CatRepository catRepository,
                         UserRepository userRepository) {
        this.catRepository = catRepository;
        this.userRepository = userRepository;
    }

    private void checkLevelUp(Cat cat) {

        if(cat.getHunger() >= 80 && cat.getEnergy() >= 80) {
            cat.setLevel(cat.getLevel() + 1);

            //レベルアップ後に少し減らす
            cat.setHunger(50);
            cat.setEnergy(50);
        }
    }

    //猫登録画面
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("cat", new Cat());
        return "cat-form";
    }

    @PostMapping("/new")
    public String createCat(@ModelAttribute Cat cat,
                            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //初期値設定
        cat.setLevel(1);
        cat.setHunger(50);
        cat.setEnergy(50);
        cat.setStamina(50);
        cat.setUser(user);
        cat.setImageStatus("normal");

        catRepository.save(cat);

        return "redirect:/cats";
    }

    @GetMapping
    public String listCats(Model model,
                           Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not fuond"));

        model.addAttribute("cats", catRepository.findByUser(user));

        return "cat-list";
    }

    //ごはんをあげる
    @ResponseBody
    @PostMapping("/{id}/feed")
    public Map<String, Object> feedCat(@PathVariable Long id) {

        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cat not found"));

        Map<String, Object> response = new HashMap<>();

        //行動制限
        if (cat.getStamina() <= 0) {
            response.put("message", "疲れて動けないにゃ");

            response.put("hunger", cat.getHunger());
            response.put("energy", cat.getEnergy());
            response.put("stamina", cat.getStamina());
            response.put("level", cat.getLevel());
            response.put("actionBlocked", true);

            return response;
        }

        int up = rand.nextInt(16) + 15;

        //満腹度を増やす(最大100)
        int newHunger = Math.min(cat.getHunger() + up, 100);
        cat.setHunger(newHunger);
        cat.setStamina(Math.max(cat.getStamina() - 10, 0));
        cat.setImageStatus("eating");

        checkLevelUp(cat);

        catRepository.save(cat);

        response.put("message", "ごはんをあげました！");
        response.put("hunger", cat.getHunger());
        response.put("energy", cat.getEnergy());
        response.put("stamina", cat.getStamina());
        response.put("imageStatus", cat.getImageStatus());
        response.put("level", cat.getLevel());

        if (up >= 28) {
            response.put("bonusMessage", "とってもおいしい！");
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/{id}/play")
    public Map<String, Object> playWithCat(@PathVariable Long id) {

        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cat not found"));

        Map<String, Object> response = new HashMap<>();

        //行動制限
        if (cat.getStamina() <= 0) {
            response.put("message", "疲れて動けないにゃ");

            response.put("hunger", cat.getHunger());
            response.put("energy", cat.getEnergy());
            response.put("stamina", cat.getStamina());
            response.put("level", cat.getLevel());
            response.put("actionBlocked", true);

            return response;
        }

        int up = rand.nextInt(16) + 15;

        //元気を増やす
        int newEnergy = Math.min(cat.getEnergy() + up, 100);
        cat.setEnergy(newEnergy);
        cat.setStamina(Math.max(cat.getStamina() - 15, 0));
        cat.setImageStatus("playing");

        //レベルアップ判定
        checkLevelUp(cat);

        catRepository.save(cat);

        response.put("message", "一緒にあそびました！");
        response.put("hunger", cat.getHunger());
        response.put("energy", cat.getEnergy());
        response.put("stamina", cat.getStamina());
        response.put("imageStatus", cat.getImageStatus());
        response.put("level", cat.getLevel());

        if (up >= 28) {
            response.put("bonusMessage", "楽しく遊びました！");
        }

        return response;
    }

    @PostMapping("/{id}/delete")
    public String deleteCat(@PathVariable Long id,
                            Authentication authentication) {

        String username = authentication.getName();

        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cat not found"));

        //自分の猫かチェック
        if (!cat.getUser().getUsername().equals(username)) {
            throw new RuntimeException("権限がありません");
        }

        catRepository.delete(cat);

        return "redirect:/cats";
    }

    @ResponseBody
    @PostMapping("/{id}/sleep")
    public Map<String, Object> sleepCat(@PathVariable Long id) {

        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cat not found"));

        int up = rand.nextInt(16) + 15;

        //体力回復(最大100)
        int newStamina = Math.min(cat.getStamina() + up, 100);
        cat.setStamina(newStamina);
        cat.setImageStatus("sleeping");

        catRepository.save(cat);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "ぐっすり寝ました！");
        response.put("stamina", cat.getStamina());
        response.put("imageStatus", cat.getImageStatus());

        if (up >= 28) {
            response.put("bonusMessage", "ぐっすり寝ました！");
        }

        return response;
    }
}
