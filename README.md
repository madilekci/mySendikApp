Bu Uygulama kullanım amacıyla yazılmamıştır.


Bu program sendikaya veya vakıf gibi bir sivil toplum kuruluşunun; organizasyon,iletişim, etkileşim gibi bir çok ihtiyacını karşılayabileceği bir mobil uygulama olarak hazırlanmıştır.Eğer bu gerçek bir projeye çevirilecek olursa bir web sitesi ve hem mobil uygulamayı hem de web sitesini yönetebileceğiniz bir panel oluşturulması planlanmıştır ancak geliştirme aşamasında kolaylık olması açısından panel yerine taslak PHP dosyalarıyla çalışılmıştır..

Uygulamanın tamamlanan fonksiyonları ;

○ Kullanıcılar uygulamaya STK tarafından verilen kullanıcı adı ve şifreleri ile giriş yaparlar.

○ Üyeler mobil uygulama üzerinden STK nın yayınlamış olduğu ; videolar,piknik veya gezilerden fotoğraflar, haberler, etkinlikler, üyelere özel indirim bulunan yerler, STK nın sosyal tesisleri gibi bir çok özelliğe erişim sağlarlar. (eğer bu STK bir sendika ise kişi kendi iş sözleşmesini de görebilir.)

○ Üyeler sosyal tesislerden randevu alabilir, oluşturulan etkinliklere katılıp katılmama durumunu bildirebilir, anketlere katılabilir, talep veya şikayetlerini iletebilirler.

○ Stk temsilcilerinin web sitesine koymak için panelden oluşturdukları haberler ve etkinlikler, JSON formatında post/get gibi methodlar ile uygulamaya gönderilir ve kullanıcılara mobil uygulama üzerinden de gösterilir.

○ Stk temsilcileri kendi temsil ettikleri üyelere, veya belirli bir şehirdeki üyelere, sadece kızlara sadece erkeklere gibi özelleştirilmiş bir çok gruba bildirimler gönderebilir. Bu bildirimlere haberleri veya etkinlikleri bağlayabilir.
Hangi bildirimin kaç kişi tarafından görüldüğü, bildirime bağlı bir haber varsa bu habere kaç kişinin gittiği gibi bazı feedback leri alabilirler.



Bu projenin katkılarından bazıları ;

♣ RecyclerView , cardView gibi temel android objelerini ve çeşitli layoutları detaylı özelleştirmeler yaparak kullandım.

♠ Android fotoğraf kütüphaneleriyle çalıştım. Fotoğrafları sıkıştırdım/şifreledim/gönderdim veya uygulamamda görüntüledim.

♣ JSON yapısında olan haber verilerini sunucumdan istedim ve uygulamamda gösterdim.

♠ Üyelik, giriş yapma kullanıcı bilgilerinin doğrulanması ve güvenliği konularında çalıştım.

♣ Firebase MS kullanarak bildirim gönderilmesi ve cihazda gösterilmesi, bildirim içeriğinin düzenlenmesi gibi konularda çalıştım.

♣ Android işletim sisteminin nasıl çalıştığı ve programları nasıl çalıştırdığı ile ilgili detaylı bilgi sahibi oldum.
