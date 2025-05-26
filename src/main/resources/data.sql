-- 메뉴 데이터 삽입
INSERT INTO menus (name, description, price, category, image_url) VALUES
('불고기', '한국의 전통적인 구이 요리', 15000.0, '한식', '/images/bulgogi.jpg'),
('비빔밥', '다양한 나물과 고기가 들어간 건강한 한식', 12000.0, '한식', '/images/bibimbap.jpg'),
('치킨', '바삭한 튀김옷의 한국식 치킨', 18000.0, '치킨', '/images/chicken.jpg'),
('피자', '다양한 토핑이 올라간 이탈리안 피자', 20000.0, '피자', '/images/pizza.jpg'),
('샐러드', '신선한 채소와 과일이 들어간 건강식', 10000.0, '샐러드', '/images/salad.jpg');

-- 알레르기 정보 추가
INSERT INTO menu_allergens (menu_id, allergen) VALUES
(1, '대두'),
(1, '밀'),
(2, '대두'),
(2, '계란'),
(3, '계란'),
(3, '밀'),
(4, '우유'),
(4, '밀'),
(5, '견과류'); 