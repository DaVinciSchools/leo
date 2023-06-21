import 'swiper/css/bundle';

import {Tab, TabProps, Tabs, TabsProps} from '@mui/material';
import {ReactNode, useEffect, useState} from 'react';
import {
  Swiper,
  SwiperClass,
  SwiperProps,
  SwiperSlide,
  SwiperSlideProps,
} from 'swiper/react';

export function TabbedSwiper<K>(props: {
  tabs: {key: K; label: ReactNode; content: ReactNode}[];
  onTabChange?: (key: K) => void;
  containerStyle?: React.CSSProperties;
  swiperProps?: SwiperProps;
  swiperSlideProps?: SwiperSlideProps;
  tabsProps?: TabsProps;
  tabProps?: TabProps;
}) {
  const [activeTab, setActiveTab] = useState(0);
  const [tabSwiper, setTabSwiper] = useState<SwiperClass | undefined>();

  useEffect(() => {
    if (tabSwiper && !tabSwiper.destroyed) {
      tabSwiper.slideTo(activeTab);
    }
    if (props.onTabChange) {
      props.onTabChange(props.tabs[activeTab].key);
    }
  }, [activeTab]);

  return (
    <>
      <Tabs
        value={activeTab}
        onChange={(e, newValue) => setActiveTab(newValue)}
        {...props.tabsProps}
      >
        {props.tabs.map(tab => (
          <Tab key={String(tab.key)} label={tab.label} {...props.tabProps} />
        ))}
      </Tabs>
      <Swiper
        onSwiper={setTabSwiper}
        slidesPerView={1}
        loop={true}
        onSlideChange={swiper => setActiveTab(swiper.realIndex)}
        {...props.swiperProps}
      >
        {props.tabs.map(tab => (
          <SwiperSlide key={String(tab.key)}>{tab.content}</SwiperSlide>
        ))}
      </Swiper>
    </>
  );
}
