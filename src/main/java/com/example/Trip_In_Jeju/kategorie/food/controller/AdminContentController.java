package com.example.Trip_In_Jeju.kategorie.food.controller;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.activity.service.ActivityService;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.attractions.service.AttractionsService;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.other.service.OtherService;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.kategorie.shopping.service.ShoppingService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/content")
public class AdminContentController {
    private final FoodService foodService;
    private final DessertService dessertService;
    private final ShoppingService shoppingService;
    private final ActivityService activityService;
    private final AttractionsService attractionsService;
    private final FestivalsService festivalsService;
    private final OtherService otherService;
    private final MemberService memberService;



    @GetMapping("/create")
    public String create(Model model) {
        List<Food> foodList = foodService.getList();
        List<Dessert> dessertList = dessertService.getList();
        List<Shopping> shoppingList = shoppingService.getList();
        List<Activity> activityList = activityService.getList();
        List<Attractions> attractionsList = attractionsService.getList();
        List<Festivals> festivalsList = festivalsService.getList();
        List<Other> otherList = otherService.getList();



        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        model.addAttribute("foodList", foodList);
        model.addAttribute("dessertList", dessertList);
        model.addAttribute("shoppingList", shoppingList);
        model.addAttribute("activityList", activityList);
        model.addAttribute("attractionsList", attractionsList);
        model.addAttribute("festivalsList", festivalsList);
        model.addAttribute("otherList", otherList);




        return "adm/content/create";
    }
    @PostMapping("/create")
    public String createContent(
            @RequestParam("title") String title,
            @RequestParam("businessHoursStart") String businessHoursStart,
            @RequestParam("businessHoursEnd") String businessHoursEnd,
            @RequestParam("content") String content,
            @RequestParam("place") String place,
            @RequestParam("closedDay") String closedDay,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("websiteUrl") String websiteUrl,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "subCategory", required = false) String subCategory,
            @RequestParam("hashtags") String hashtags,
            @RequestParam("category") String category,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam(value = "periodStart", required = false) String periodStart,
            @RequestParam(value = "periodEnd", required = false) String periodEnd) {

        if (category.equals("food")) {
            foodService.create(title, businessHoursStart, businessHoursEnd, content, place, closedDay, websiteUrl, phoneNumber, hashtags, thumbnail, latitude, longitude, subCategory);
        } else if (category.equals("dessert")) {
            dessertService.create(title, businessHoursStart, businessHoursEnd, content, place, closedDay, websiteUrl, phoneNumber, hashtags, thumbnail, latitude, longitude, subCategory);
        }
        else if (category.equals("shopping")) {
            shoppingService.create(title, businessHoursStart, businessHoursEnd, content, place, closedDay, websiteUrl, phoneNumber, hashtags, thumbnail, latitude, longitude, subCategory);
        }
        else if (category.equals("activity")) {
            activityService.create(title, businessHoursStart, businessHoursEnd, content, place, closedDay, websiteUrl, phoneNumber, hashtags, thumbnail, latitude, longitude, subCategory);
        }
        else if (category.equals("attractions")) {
            attractionsService.create(title, businessHoursStart, businessHoursEnd, content, place, closedDay, websiteUrl, phoneNumber, hashtags, thumbnail, latitude, longitude, subCategory);
        }
        else if (category.equals("festivals")) {
            festivalsService.create(title, periodStart, periodEnd, content, place, closedDay, websiteUrl, phoneNumber, hashtags, thumbnail, latitude, longitude, subCategory);
        }
        else if (category.equals("other")) {
            otherService.create(title, businessHoursStart, businessHoursEnd, content, place, closedDay, websiteUrl, phoneNumber, hashtags, thumbnail, latitude, longitude, subCategory);
        }

        return "redirect:/adm/content/create";
    }
}

//    @PostMapping("/createFood")
//    public String createFoodContent(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("place") String place, @RequestParam("closedDay") String closedDay, @RequestParam("thumbnail") MultipartFile thumbnail) {
//        foodService.create(title, content, place, closedDay, thumbnail);
//        return "redirect:/adm/food/create";
//    }
//
//    @PostMapping("/createDessert")
//    public String createDessertContent(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("place") String place, @RequestParam("closedDay") String closedDay,  @RequestParam("thumbnail") MultipartFile thumbnail) {
//        dessertService.create(title, content, place, closedDay, thumbnail);
//        return "redirect:/adm/food/create";
//    }
